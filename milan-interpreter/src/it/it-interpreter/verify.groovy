String log = new File(basedir, 'build.log').text;
[
  "65433214",
].each { assert log.contains(it): "Log doesn't contain ['$it']" }
true