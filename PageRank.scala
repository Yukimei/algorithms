import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object PageRank {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("task3")
    val sc = new SparkContext(conf)
    val textfile = sc.textFile("hdfs:///input").distinct()

    val links = textfile.map{ s =>
      val parts = s.split("\t")
      (parts(0), parts(1))
    }.distinct().groupByKey().cache()
    var userWithFollowers = links.values.flatMap(x => x).distinct()


    var allNodes = textfile.flatMap(line => line.split("\t")).distinct()
    var ranks = allNodes.map(v => (v, 1.0))
    var nonDanglingNodes = links.keys

    var danglingNodes = allNodes.subtract(nonDanglingNodes).collect()
    var userWithoutFollowers = allNodes.subtract(userWithFollowers).collect()

    var danglingList = for (i <- danglingNodes) yield (i, List("dangle"))
    var userWithoutFollowersList = for (i <- danglingNodes) yield (i, 0.0)

    var allLinks = links ++ sc.parallelize(danglingList)

    var numNodes = allNodes.count()

    for (i <- 1 to 10) {
      var danSum = sc.accumulator(0.0)

      var contribs = allLinks.join(ranks).values.flatMap{ case (urls, rank) =>
          if (urls.mkString==("dangle")) {
              danSum += rank
              List()
          } else {
              val size = urls.size
              urls.map(url => (url, rank / size))
          }
      }

      val danglingConvibuteVal = danSum.value
      val danContrib = danglingConvibuteVal / numNodes

      ranks = contribs.union(sc.parallelize(userWithoutFollowersList))
                  .reduceByKey(_ + _)
                  .mapValues(x => 0.15 + 0.85 * (x + danContrib)) 
    }

    val output = ranks.map(y => y._1 + "\t" + y._2).saveAsTextFile("hdfs:///pagerank-output")
  }
}