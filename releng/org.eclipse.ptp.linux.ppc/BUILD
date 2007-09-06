#!/bin/sh
#
# This script is used to build any executable fragments in the workspace
#
export KNOWN_FRAGMENTS="\
	org.eclipse.ptp.utils\
	org.eclipse.ptp.proxy\
	org.eclipse.ptp.debug.sdm\
	org.eclipse.ptp.orte.proxy\
	org.eclipse.ptp.mpich2.proxy\
	org.eclipse.ptp.utils_1.1.0\
	org.eclipse.ptp.proxy_1.1.0\
	org.eclipse.ptp.debug.sdm_1.1.0\
	org.eclipse.ptp.orte.proxy_1.1.0\
	org.eclipse.ptp.mpich2.proxy_1.1.0"
	
export PARENT=`dirname $PWD`
export FRAGMENT=`basename $PWD`

export PREFIX=$PARENT/$FRAGMENT

for fragment in $KNOWN_FRAGMENTS
do
	path=$PARENT/$fragment
	if [ -d $path ]; then
		(cd $path; \
		sh configure --prefix=$PREFIX; \
		chmod +x install-sh; \
		make install)
	fi
done

exit 0
