using System;
using System.Diagnostics;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Threading;
using System.Threading.Tasks;

namespace Hadoop_ETL.Infrastructure
{
    public class StopwatchHandler: DelegatingHandler
    {
        public StopwatchHandler(HttpMessageHandler innerHandler = null)
        {
            InnerHandler = innerHandler ?? new HttpClientHandler();
        }
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            var stopwach = new Stopwatch();
            Console.WriteLine($"Starting request to: {request.Method} {request.RequestUri}");
            stopwach.Start();
            var response = await base.SendAsync(request, cancellationToken);
            stopwach.Stop();
            Console.WriteLine($"Request finished, status: {response.StatusCode:G}, time elapsed: {stopwach.Elapsed}");

            if (response.StatusCode >= HttpStatusCode.BadRequest)
            {
                var message = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Request failed, message: {message}");
            }
            return response;
        }
    }
}