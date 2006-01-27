/******************************************************************************
 * Copyright (c) 2005 The Regents of the University of California.
 * This material was produced under U.S. Government contract W-7405-ENG-36
 * for Los Alamos National Laboratory, which is operated by the University
 * of California for the U.S. Department of Energy. The U.S. Government has
 * rights to use, reproduce, and distribute this software. NEITHER THE
 * GOVERNMENT NOR THE UNIVERSITY MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified
 * to produce derivative works, such modified software should be clearly  
 * marked, so as not to confuse it with the version available from LANL.
 *
 * Additionally, this program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * LA-CC 04-115
 ******************************************************************************/
 
#ifndef _COMPAT_H_
#define _COMPAT_H_

#include <sys/types.h>

#ifndef MAX
#define MAX(a, b)		((a) < (b) ? (b) : (a))
#endif /* MAX */

#ifndef MIN
#define MIN(a, b)		((a) > (b) ? (b) : (a))
#endif /* MAX */

#ifdef __APPLE__
#define BITSPERBYTE     NBBY
#elif __linux__
#include <limits.h>
#define BITSPERBYTE     CHAR_BIT
extern int digittoint(int);
#else
#error "Need to define BITSPERBYTE for your operating system"
#endif

#define SOCKET			int
#define CLOSE_SOCKET(s)	(void)close(s)
#define INVALID_SOCKET	-1
#define SOCKET_ERROR		-1

#endif /* _COMPAT_H_*/
