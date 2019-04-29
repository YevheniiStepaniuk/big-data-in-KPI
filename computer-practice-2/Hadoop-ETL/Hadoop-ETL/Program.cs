using System;
using System.IO;
using ChoETL;
using Hadoop_ETL.Infrastructure;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json.Linq;

namespace Hadoop_ETL
{
    class Program
    {
        static void Main(string[] args)
        {
            var config = LoadConfig();

            var options = config.Get<ETLOptions>();

            var nyTimesClient = new NyTimesClient(options.NyTimes);

            var dateFrom = new DateTime(2012, 1, 1);
            var dateTo = new DateTime(2013, 4, 1);

            var hadoopClient = new HDFSClient(options.Hadoop);

            if (!Directory.Exists(options.NyTimes.FileStorePath))
            {
                Directory.CreateDirectory(options.NyTimes.FileStorePath);
            }

            do
            {
                JArray items = nyTimesClient.LoadItemsFromArchive(dateFrom).Result;

                var fileName = $"ny-times-{dateFrom:yyyy-MM}.csv";
                var directoryName = $"dt={dateFrom:yyyy-MM}";

                var storeDirectory = Path.Combine(options.NyTimes.FileStorePath, directoryName);
                var hdfsDirecoryPath = Path.Combine(options.Hadoop.FolderPath, directoryName);
                var hdfsFilePath = Path.Combine(hdfsDirecoryPath, fileName);

                var filePath = Path.Combine(options.NyTimes.FileStorePath, fileName);

                if (!hadoopClient.DirectoryExist(hdfsDirecoryPath).Result)
                {
                    hadoopClient.CreateDirectory(hdfsDirecoryPath).Wait();
                }

                if (File.Exists(filePath))
                {
                    hadoopClient.UploadFile(hdfsFilePath, filePath, true);
                }
                else
                {
                    var itemsStream = new MemoryStream();
                    using (var r = ChoJSONReader.LoadText(items.ToString()))
                    {
                        using (var w = new ChoCSVWriter(itemsStream).WithFirstLineHeader())
                        {
                            w.Write(r);
                        }
                    }

                    using (var fileStream = File.Create(filePath))
                    {
                        itemsStream.Seek(0, SeekOrigin.Begin);
                        itemsStream.CopyTo(fileStream);
                    }

                    hadoopClient.UploadFile(hdfsFilePath, itemsStream, true).Wait();
                }

                dateFrom = dateFrom.AddMonths(1);

            } while (dateFrom.Month <= dateTo.Month);
            

            Console.ReadKey();
        }

        private static IConfigurationRoot LoadConfig()
        {
            var environmentName =
                Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT") ??
                Environment.GetEnvironmentVariable("environment") ??
                "local";

            Console.WriteLine($"Current ENV is: {environmentName}");

            return new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", true, true)
                .AddJsonFile($"appsettings.{environmentName}.json", true, true)
                .AddEnvironmentVariables()
                .Build();
        }
    }
}
