#!/bin/sh

# This script uses the Minio client to populate Minio with test data.  The Minio command line client must
# already be installed and have the correct permissions to be run.

# Quickstart guide: https://docs.min.io/docs/minio-client-quickstart-guide.html
# Archive: https://dl.min.io/client/mc/release/

usage()
{
  echo "Usage: $0 [ -c MINIO_CLIENT_HOME ]"
  exit 2
}

while getopts 'c:?h' option
do
  case ${option} in
    c) mc_home=$OPTARG ;;
    h|?) usage ;;
  esac
done

SCRIPT_DIR=$(dirname $0)
echo $SCRIPT_DIR

set -o errexit

echo 'Configuring Minio localhost endpoint...'
$mc_home/mc alias set minio http://127.0.0.1:30090 root root1234 --api S3v4

echo 'Creating buckets...'
$mc_home/mc mb minio/data-bucket --region=us-west-1 || true

echo 'Copying input data to buckets...'
$mc_home/mc cp $SCRIPT_DIR/../data/declaration_of_independence.txt minio/data-bucket/wordcount/input/
