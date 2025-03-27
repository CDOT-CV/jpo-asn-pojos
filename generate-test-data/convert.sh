#!/usr/bin/env bash

set -x

BASEDIR=./examples/2024/bsm
./converter-debug -p $1 -iuper -oxer $BASEDIR/$2.bin > $BASEDIR/$2.xml
./converter-debug -p $1 -iuper -ojer $BASEDIR/$2.bin > $BASEDIR/$2.json
