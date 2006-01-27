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

/*
 * Miscellaneous proxy functions.
 */
 
 #include <stdlib.h>
 
 #include "proxy.h"

extern proxy_svr_funcs 	proxy_tcp_svr_funcs;

proxy proxies[] = {
	{"tcp", NULL, NULL,  &proxy_tcp_svr_funcs},
	{NULL, NULL, NULL, NULL}
};

int
proxy_svr_init(char *name, proxy_handler_funcs *pf, proxy_svr_helper_funcs *sf, proxy_svr_commands *cmds, proxy_svr **svr)
{
	proxy *		p;
	proxy_svr *	ps;
	void *		data;
	
	if (find_proxy(name, &p) < 0)
		return PROXY_RES_ERR;
		
	p->handler_funcs = pf;

	ps = (proxy_svr *)malloc(sizeof(proxy_svr));
	ps->proxy = p;
	ps->svr_helper_funcs = sf;
	ps->svr_commands = cmds;
	
	if (p->svr_funcs->init(ps, &data) < 0) {
		free(ps);
		return PROXY_RES_ERR;
	}

	ps->svr_data = data;
	
	*svr = ps;
	
	return PROXY_RES_OK;	
}

int
proxy_svr_create(proxy_svr *ps, int port)
{
	if (ps != NULL)
		return ps->proxy->svr_funcs->create(ps, port);
		
	return PROXY_RES_ERR;
}

int
proxy_svr_connect(proxy_svr *ps, char *host, int port)
{
	if (ps != NULL)
		return ps->proxy->svr_funcs->connect(ps, host, port);
		
	return PROXY_RES_ERR;
}

int
proxy_svr_progress(proxy_svr *ps)
{
	if (ps != NULL)
		return ps->proxy->svr_funcs->progress(ps);
		
	return PROXY_RES_ERR;
}

void
proxy_svr_finish(proxy_svr *ps)
{
	if (ps != NULL)
		ps->proxy->svr_funcs->finish(ps);
}

void
proxy_svr_event_callback(proxy_svr *ps, char *data)
{
	if (ps != NULL)
		proxy_event_callback(ps->proxy, data);
}
