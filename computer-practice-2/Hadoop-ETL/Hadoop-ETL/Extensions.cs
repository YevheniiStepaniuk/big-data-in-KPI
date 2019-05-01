using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using Newtonsoft.Json;

namespace Hadoop_ETL
{
    internal static class Extensions
    {
        public static Stream ToStream(this string text)
        {
            byte[] byteArray = Encoding.ASCII.GetBytes(text);
            return new MemoryStream(byteArray);
        }

        public static bool IsDefined(this string str)
        {
            return !string.IsNullOrWhiteSpace(str);
        }

        public static string ToUrl(this Dictionary<string, string> dictionary, string query = "")
        {
            var result = new List<string>();
            foreach (var element in dictionary)
            {
                result.Add(element.Key + "=" + element.Value);
            }

            return query.StartsWith("?") ? 
                $"{query}&{string.Join('&', result)}" : 
                "?" + (query.IsDefined() ? $"{query}&" : query) 
                    + string.Join('&',result);
        }

        public static Dictionary<T, U> IntoDictionary<T, U>(this IEnumerable<(T, U)> values, params (T,U)[] items)
        {
            var dictionary = new Dictionary<T, U>();
            values = values ?? new (T, U)[0];
            items = items ?? new (T, U)[0];

            foreach (var (key, value) in values)
            {
                dictionary.Add(key, value);
            }

            foreach (var (key, value) in items)
            {
                dictionary.TryAdd(key, value);
            }

            return dictionary;
        }

        public static IEnumerable<Exception> GetInnerExceptions(this Exception ex)
        {
            if (ex == null)
                yield break;
            var innerException = ex;
            do
            {
                yield return innerException;
                innerException = innerException.InnerException;
            }
            while (innerException != null);
        }

        public static void WriteToConsole(this Exception exception)
        {
            foreach (var innerException in exception.GetInnerExceptions())
            {
                Console.WriteLine(innerException.Message);
            }
        }

        public static string ToHadoopJson<T>(this IEnumerable<T> items)
        {
            var builder = new StringBuilder();

            foreach (var item in items)
            {
                builder.Append(JsonConvert.SerializeObject(item, Formatting.None));
                builder.Append('\n');
            }

            return builder.ToString();
        }
    }
}