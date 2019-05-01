using System;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;
using Bogus.DataSets;
using Hadoop_ETL.Infrastructure;
using Newtonsoft.Json.Linq;

namespace Hadoop_ETL.Generator
{
    public class FakeNewsProcessor
    {
        private readonly ETLOptions _options;
        private readonly FakeNewsGenerator _generator;
        private readonly HDFSClient _hdfsClient;

        public FakeNewsProcessor(ETLOptions options, FakeNewsGenerator generator, HDFSClient hdfsClient)
        {
            _options = options;
            _generator = generator;
            _hdfsClient = hdfsClient;
        }

        public async Task Process(int minCount = 60000, int maxCount = 100000, DateTime? dateFrom = null, DateTime? dateTo = null)
        {
            var stopWatch = new Stopwatch();

            var dateToValue = dateTo ?? DateTime.UtcNow;
            var dateFromValue = dateFrom ?? dateToValue.AddYears(-1);

            Console.WriteLine($"Start generating items: {dateFromValue:Y} - {dateToValue:Y}");

            do
            {
                stopWatch.Start();

                var items = _generator.Generate(minCount, maxCount);

                var fileName = $"fake-news-{dateFrom:yyyy-MM}.json";
                var directoryPath = string.Format(_options.Hadoop.NewsFolderPath, dateFromValue.Year, $"{dateFromValue:MMMM}");

                var filePath = Path.Combine(directoryPath, fileName);

                var directoryExist = await _hdfsClient.DirectoryExist(directoryPath);

                if (!directoryExist)
                {
                    await _hdfsClient.CreateDirectory(directoryPath);
                }

                var json = items.ToHadoopJson().ToStream();

                var result = await _hdfsClient.UploadFile(filePath, json, true);

                dateFromValue = dateFromValue.AddMonths(1);
                stopWatch.Stop();

                Console.WriteLine(result
                    ? $"Item processed to {filePath}, elapsed: {stopWatch.Elapsed}"
                    : $"Item not processed to {filePath}, elapsed: {stopWatch.Elapsed}");

                stopWatch.Reset();

            } while (dateFromValue <= dateToValue);
        }
    }
}