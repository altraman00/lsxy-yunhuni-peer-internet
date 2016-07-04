cd ../
git pull
mvn clean compile install -Dmaven.test.skip=true -Pdevelopment
cd bin
