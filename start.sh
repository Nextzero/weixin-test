#!/bin/sh
myself=`which $0`
myself=`dirname ${myself}`
myself=`cd "$myself"; pwd`

root=$myself

#运行
#-------------------------------------------------------------------------
cd $root
java -Dfile.encoding=UTF-8 -cp $root/lib/*:$root/weixin-test-1.0-SNAPSHOT.jar nextzero.weixin.test.ZTestMain
#-------------------------------------------------------------------------
