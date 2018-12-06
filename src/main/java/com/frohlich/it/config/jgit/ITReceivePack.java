package com.frohlich.it.config.jgit;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

public class ITReceivePack implements ReceivePackFactory<HttpServletRequest> {

	@Override
	public ReceivePack create(HttpServletRequest req, Repository db)
			throws ServiceNotEnabledException, ServiceNotAuthorizedException {

		return null;
	}

}
