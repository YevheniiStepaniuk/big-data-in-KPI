namespace Hadoop_ETL.Infrastructure
{
    public class ETLOptions
    {
        public HadoopOptions Hadoop { get; set; }
        public NyTimesOptions NyTimes { get; set; }
        public FakeNewsOptions FakeNews { get; set; }
        public ApplicationMode Mode { get; set; }
    }

    public enum ApplicationMode
    {
        Generator,
        ETL
    }
}