using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

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
    }
}