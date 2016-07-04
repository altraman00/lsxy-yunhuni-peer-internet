
git pull
EXP_SCRIPT=""
if [ $# == 1 ]; then
  echo "参数个数：$#"
  EXP_SCRIPT=" -pl $1 -am"
fi
cd ../
echo "mvn clean compile install -Dmaven.test.skip=true -Pdevelopment $EXP_SCRIPT"
mvn clean compile install -Dmaven.test.skip=true -Pdevelopment $EXP_SCRIPT
cd bin
