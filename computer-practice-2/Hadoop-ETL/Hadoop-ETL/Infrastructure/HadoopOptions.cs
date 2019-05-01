namespace Hadoop_ETL.Infrastructure
{
    public class HadoopOptions
    {
        public string ServerUrl { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string NewsFolderPath { get; set; }
        public string NyTimesFolderPath { get; set; }
        public string[] DataNodesUrl { get; set; }
    }
}