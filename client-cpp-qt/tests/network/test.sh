#!/bin/bash

# script for testing NetworkManager::downloadByUrl()
# reads list of requests from input file
# makes requests, compares gold and output files

# define constants
PROGRAM=network/network
OUTPUT_DIR=out
GOLD_DIR=gold
FORMAT=xml

#check arguments
if (($# < 1)) 
then echo "Not found input file"
     echo "Usage: ./script [file(list of requests)]"
     exit 0
fi

#read requests from input file
declare -a requests
requests=(`cat "$1"`)
#echo ${requests[@]}

#run program for each file
i=0
for request in ${requests[@]}
do
    out=$OUTPUT_DIR'/'out$i
    gold=$GOLD_DIR'/'gold$i
   `'./'$PROGRAM $request $out`

#compare gold and out files
    if (`cmp $gold $out`)
    then
        echo 'PASSED'
    else
        echo 'FAILED'
    fi
    let i=$i+1
done

