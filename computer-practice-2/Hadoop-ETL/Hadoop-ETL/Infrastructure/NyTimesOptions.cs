
namespace Hadoop_ETL.Infrastructure
{
    public class NyTimesOptions
    {
        public string ApiKey { get; set; }
        public string ArchiveUrl { get; set; }
        public string FileStorePath { get; set; }

        public int YearFrom { get; set; }
        public int MonthFrom { get; set; }

        public int YearTo { get; set; }
        public int MonthTo { get; set; }
    }
}
