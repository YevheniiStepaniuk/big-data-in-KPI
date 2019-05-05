
import java.sql._
import java.text.SimpleDateFormat

/**
  *
  * Created by Pulkit on 18Mar2019
  *
  * Content of this file is proprietary and confidential.
  * It shall not be reused or disclosed without prior consent
  * of distributor
  **/

object MyJdbcClient {

  var postgresClient: Connection = null
  val POSTGRES_HOST = ""
  val POSTGRES_DB = "my_database"
  val POSTGRES_TGT_TABLE = "my_tweets"
  val POSTGRES_USER = "my_user"
  val POSTGRES_PASSWORD = "BadPractice123"


  def verifyConnection(): Unit = {
    if (postgresClient == null || postgresClient.isClosed) {

      Class.forName("org.postgresql.Driver")

      try {
        postgresClient = DriverManager.getConnection(s"jdbc:postgresql://$POSTGRES_HOST/$POSTGRES_DB", POSTGRES_USER, POSTGRES_PASSWORD)

      } catch {
        case e: Exception =>
          e.printStackTrace()
          System.err.println(e.getClass.getName + ": " + e.getMessage)
          System.exit(0)
          postgresClient.close()
      }
    }
  }


  def loadTweets(tweetStatusesAndScore: List[(MyTwitterClient.TwitterStatus, Int)]): Unit = {
    val preparedStatement = postgresClient.prepareStatement(s"INSERT INTO $POSTGRES_TGT_TABLE VALUES (?,?,?,?,?,?,?,?)")
    tweetStatusesAndScore.foreach { ele =>

      //date format
      val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

      val status = ele._1

      preparedStatement.setLong(1, status.statusId)
      preparedStatement.setLong(2, status.userId)
      preparedStatement.setString(3, status.userName)
      preparedStatement.setTimestamp(4, new Timestamp(df.parse(status.createdAt).getTime))
      preparedStatement.setString(5, status.language)
      preparedStatement.setInt(6, status.favoriteCount)
      preparedStatement.setArray(7, postgresClient.createArrayOf("text", status.hashTags.toArray))
      preparedStatement.setInt(8, ele._2)
      preparedStatement.addBatch()
    }
    val results = preparedStatement.executeBatch
    System.out.println("===>" + results.sum + " records inserted.")
  }

  def verifyTweetsTable(): Unit = {

    try {
      val tableResultSet =
        postgresClient
          .getMetaData
          .getTables(null, null, POSTGRES_TGT_TABLE, null)
      println("Going through table list")

      if (tableResultSet.next()) {
        System.out.println("Tweets table exists!")
      } else {
        System.out.println("I am going to create Tweets table.")
        createTweetsTable()
      }
      tableResultSet.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        System.err.println(e.getClass.getName + ": " + e.getMessage)
        postgresClient close()
    }
  }

  def createTweetsTable(): Unit = {
    var statement: Statement = null
    try {
      statement = postgresClient.createStatement
      val sql =
        s"CREATE TABLE $POSTGRES_TGT_TABLE(" +
          " STATUS_ID      BIGINT PRIMARY KEY  NOT NULL," +
          " USER_ID        BIGINT NOT NULL," +
          " USER_NAME      TEXT," +
          " CREATED_AT     TIMESTAMP," +
          " LANGUAGE       TEXT," +
          " FAVORITE_COUNT INT," +
          " HASHTAGS       TEXT[]," +
          " SCORE          REAL)"
      statement.executeUpdate(sql)
      statement.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        System.err.println(e.getClass.getName + ": " + e.getMessage)
        postgresClient close()
        statement.close()
    }
  }

}