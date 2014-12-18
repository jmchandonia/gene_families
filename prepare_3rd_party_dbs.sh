#!/bin/bash
cd ./data
mkdir tmp
cd ./tmp
unamestr=`uname`

if [[ "$unamestr" == 'Linux' ]]; then
    OS='linux'
elif [[ "$unamestr" == 'Darwin' ]]; then
    OS='macos'
fi

########### Pfam #############
if [ ! -f ../db/Pfam-A.hmm ]; then
    echo "Downloading Pfam..."
    curl -o ../db/Pfam-A.full.gz 'ftp://ftp.ebi.ac.uk/pub/databases/Pfam/releases/Pfam27.0/Pfam-A.full.gz'
    curl -o ../db/Pfam-A.hmm.gz 'ftp://ftp.ebi.ac.uk/pub/databases/Pfam/releases/Pfam27.0/Pfam-A.hmm.gz'
    gzip -d ../db/Pfam-A.hmm.gz
    ../bin/hmmpress.$OS ../db/Pfam-A.hmm
fi

########### TIGRFAMS #############
if [ ! -f ../db/TIGRFAMs_15.0_HMM.LIB ]; then
    echo "Downloading Pfam..."
    curl -o ../db/TIGRFAMs_15.0_HMM.LIB.gz 'ftp://ftp.jcvi.org/pub/data/TIGRFAMs/TIGRFAMs_15.0_HMM.LIB.gz'
    gzip -d ../db/TIGRFAMs_15.0_HMM.LIB.gz
    ../bin/hmmpress.$OS ../db/TIGRFAMs_15.0_HMM.LIB
fi

########### CDD #############
if [ ! -f ../db/Cdd.rps ]; then
    echo "Downloading CDD..."
    curl -o ../db/cddid.tbl.gz 'ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cddid.tbl.gz'
    curl -o cdd.tar.gz 'ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cdd.tar.gz'
    mkdir smp
    cd smp
    # need to do this in multiple steps so we don't run out of disk space
    tar --wildcards -xf ../cdd.tar.gz 'COG*.smp'
    ls -1 COG*.smp > Cog
    ../../bin/makeprofiledb.$OS -in Cog -threshold 9.82 -scale 100.0 -dbtype rps -index true
    mv Cog.* ../../db
    rm *.smp

    tar --wildcards -xf ../cdd.tar.gz 'smart*.smp'
    ls -1 smart*.smp > Smart
    ../../bin/makeprofiledb.$OS -in Smart -threshold 9.82 -scale 100.0 -dbtype rps -index true
    mv Smart.* ../../db
    rm *.smp

    tar --wildcards -xf ../cdd.tar.gz 'cd*.smp'
    ls -1 cd*.smp > Cdd
    ../../bin/makeprofiledb.$OS -in Cdd -threshold 9.82 -scale 100.0 -dbtype rps -index true
    mv Cdd.* ../../db
    rm *.smp
    cd ..
    rm -rf smp
    rm cdd.tar.gz
fi
