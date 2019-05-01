using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Hadoop_ETL.Generator
{
    public class Entity
    {
        public string Title { get; set; }
        public string Description { get; set; }
        [JsonConverter(typeof(StringEnumConverter))]
        public NewsType Type { get; set; }
        public string Language { get; set; }
        [JsonProperty("author_info")]
        public Author AuthorInfo { get; set; }
        public string Country { get; set; }
        [JsonConverter(typeof(StringEnumConverter))]
        public NewsCategory Category { get; set; }
        [JsonProperty("added_date")]
        public DateTime AddedDate { get; set; }
        public int Priority { get; set; }
        [JsonProperty("logo_info")]
        public Logo LogoInfo { get; set; }
    }

    public class Logo
    {
        public string Link { get; set; }
        public string Alt { get; set; }
    }

    public enum NewsCategory
    {
        Sport, 
        Politic,
        Economic,
        Culture,
        Music
    }

    public class Author
    {
        public string FirstName { get; set; }
        public string SecondName { get; set; }
        public string Title { get; set; }
    }

    public enum NewsType
    {
        Emergency,
        Casual
    }
}