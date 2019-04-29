namespace Hadoop_ETL.Infrastructure
{
    public class ETLOptions
    {
        public HadoopOptions Hadoop { get; set; }
        public NyTimesOptions NyTimes { get; set; }
    }
}