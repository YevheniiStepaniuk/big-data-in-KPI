using System;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using Hadoop_ETL.Infrastructure;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json.Linq;

namespace Hadoop_ETL
{
    public class NyTimesClient
    {
        private readonly HttpClient _httpClient;
        private readonly NyTimesOptions _configuration;

        public NyTimesClient(NyTimesOptions config)
        {
            _configuration = config;
            _httpClient = new HttpClient(new QueryParamAppendHandler("api-key",_configuration.ApiKey, new StopwatchHandler()));
        }

        public async Task<JArray> LoadItemsFromArchive(DateTime date)
        {
            var url = string.Format(_configuration.ArchiveUrl, date.Year, date.Month);
            var response = await _httpClient.GetStringAsync(url);

            var jToken = JObject.Parse(response);

            return jToken.SelectToken("response.docs") as JArray;
        }
    }

}