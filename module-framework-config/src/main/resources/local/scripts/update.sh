#! /bin/bash
echo -e "\n### lsxy server check ###\n"

if [ $# = 0 ];then
        echo "Use: $0 -j [start|stop|status|restart|mystart|log] -a app-portal-api -r 1.2.0-RC3 -h p5a"
        exit 1
fi



while getopts :a:r:h:j: ARGS
do
 case $ARGS in
     a)
#         echo "Found the -a option"
#         echo "The parameter follow -a is $OPTARG"
                 app_name=$OPTARG
         ;;
     r)
 #        echo "Found the -r option"
 #        echo "The parameter follow -r is $OPTARG"
                 r_name=$OPTARG
         ;;
     h)
  #       echo "Found the -h option"
  #       echo "The parameter follow -h is $OPTARG"
                 host_name=$OPTARG
         ;;
     j)
   #      echo "Found the -j option"
   #      echo "The parameter follow -j is $OPTARG"
                 action_name=$OPTARG
         ;;
     ?)
          echo "Use: $0 -j [start|stop|status|restart|mystart|log] -a app-portal-api -r 1.2.0-RC3 -h p5a"
          exit 1
         ;;
 esac
done

echo "app_name:$app_name"