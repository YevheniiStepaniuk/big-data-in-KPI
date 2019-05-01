using System;
using System.IO;
using Hadoop_ETL.Generator;
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


            var hadoopClient = new HDFSClient(options.Hadoop);

            Console.WriteLine($"Current mode is: {options.Mode}");

            if (options.Mode == ApplicationMode.ETL)
            {
                var dateFrom = new DateTime(options.NyTimes.YearFrom, options.NyTimes.MonthFrom, 1);
                var dateTo = new DateTime(options.NyTimes.YearTo, options.NyTimes.MonthTo, 1);

                var nyTimesClient = new NyTimesClient(options.NyTimes);
                var etlProcessor = new ETLProcessor(hadoopClient, nyTimesClient, options);

                etlProcessor.UploadItems(dateFrom, dateTo).Wait();
            }
            else
            {
                var dateFrom = new DateTime(options.FakeNews.YearFrom, options.FakeNews.MonthFrom, 1);
                var dateTo = new DateTime(options.FakeNews.YearTo, options.FakeNews.MonthTo, 1);

            
                var fakeNewsGenerator = new FakeNewsGenerator();
                var fakeNewsProcessor = new FakeNewsProcessor(options, fakeNewsGenerator, hadoopClient);


                fakeNewsProcessor.Process(dateFrom: dateFrom, dateTo: dateTo).Wait();
            }


            Console.ReadLine();
        }

        private static IConfigurationRoot LoadConfig()
        {
            var environmentName =
                Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT") ??
                Environment.GetEnvironmentVariable("environment") ??
                "local";

            Console.WriteLine($"Current ENV is: {environmentName}");

            var builder = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddEnvironmentVariables();

            if(environmentName == "local"){
                builder = builder                
                    .AddJsonFile("appsettings.json", true, true)
                    .AddJsonFile($"appsettings.{environmentName}.json", true, true);
            }

            return builder.Build();
        }
    }
}
