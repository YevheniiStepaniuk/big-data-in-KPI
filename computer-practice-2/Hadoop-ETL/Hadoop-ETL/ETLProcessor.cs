using System;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Hadoop_ETL.Infrastructure;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Hadoop_ETL
{
    public class ETLProcessor
    {
        private readonly HDFSClient _hdfsClient;
        private readonly NyTimesClient _nyTimesClient;
        private readonly ETLOptions _etlOptions;

        public ETLProcessor(HDFSClient hdfsClient, NyTimesClient nyTimesClient, ETLOptions etlOptions)
        {
            _hdfsClient = hdfsClient;
            _nyTimesClient = nyTimesClient;
            _etlOptions = etlOptions;
        }

        public async Task UploadItems(DateTime? dateFrom = null, DateTime? dateTo = null)
        {
            var dateToValue = dateTo ?? DateTime.UtcNow;
            var dateFromValue = dateFrom ?? dateToValue.AddYears(-1);

            Console.WriteLine($"Start items loading: {dateFromValue:Y} - {dateToValue:Y}");

            var stopWatch = new Stopwatch();
            do
            {
                stopWatch.Start();
                JArray items = await _nyTimesClient.LoadItemsFromArchive(dateFromValue);

                var fileName = $"ny-times-{dateFromValue:yyyy-MM}.json";
                var directoryPath = string.Format(_etlOptions.Hadoop.NyTimesFolderPath, dateFromValue.Year, dateFromValue.Month);

                var filePath = Path.Combine(directoryPath, fileName);

                var directoryExist = await _hdfsClient.DirectoryExist(directoryPath);

                if (!directoryExist)
                {
                    await _hdfsClient.CreateDirectory(directoryPath);
                }

                var json = FormatJsonForHadoop(items).ToStream();

                var result = await _hdfsClient.UploadFile(filePath, json, true);

                dateFromValue = dateFromValue.AddMonths(1);
                stopWatch.Stop();

                Console.WriteLine(result
                    ? $"Item processed to {filePath}, elapsed: {stopWatch.Elapsed}"
                    : $"Item not processed to {filePath}, elapsed: {stopWatch.Elapsed}");

                stopWatch.Reset();

            } while (dateFromValue <= dateToValue);
        }

        private string FormatJsonForHadoop(JArray items)
        {
            var builder = new StringBuilder();

            foreach (var item in items)
            {
                builder.Append(item.ToString(Formatting.None));
                builder.Append('\n');
            }

            return builder.ToString();
        }
    }
}