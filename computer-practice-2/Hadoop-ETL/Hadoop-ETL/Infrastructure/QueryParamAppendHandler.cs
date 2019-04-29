using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace Hadoop_ETL.Infrastructure
{
    public class QueryParamAppendHandler : DelegatingHandler
    {
        private readonly Dictionary<string, string> _params;

        public QueryParamAppendHandler(string key, string value, HttpMessageHandler innerHandler = null) :
            this(new Dictionary<string, string> {{key, value}}, innerHandler)
        {
        }

        public QueryParamAppendHandler(Dictionary<string, string> queryParams, HttpMessageHandler innerHandler = null)
        {
            _params = queryParams;
            InnerHandler = innerHandler ?? new HttpClientHandler();
        }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
            CancellationToken cancellationToken)
        {
            var url = request.RequestUri;
            var query = _params.ToUrl(url.Query);

            var builder = new UriBuilder(url) {Query = query};

            request.RequestUri = builder.Uri;

            return base.SendAsync(request, cancellationToken);
        }
    }
}