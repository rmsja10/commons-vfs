/* ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.commons.vfs.provider.ftp;

import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.UriParser;

/**
 * A parser for FTP URI.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.5 $ $Date: 2002/07/05 04:08:18 $
 */
class FtpFileNameParser
    extends UriParser
{
    /**
     * Parses an absolute URI, splitting it into its components.
     */
    public FtpUri parseFtpUri( final String uriStr )
        throws FileSystemException
    {
        final FtpUri uri = new FtpUri();

        // FTP URI are generic URI (as per RFC 2396)
        parseGenericUri( uriStr, uri );

        // Drop the port if it is 21
        final String port = uri.getPort();
        if ( port != null && port.equals( "21" ) )
        {
            uri.setPort( null );
        }

        // Split up the userinfo into a username and password
        // TODO - push this into parser and GenericUri
        final String userInfo = uri.getUserInfo();
        if ( userInfo != null )
        {
            int idx = userInfo.indexOf( ':' );
            if ( idx == -1 )
            {
                uri.setUserName( userInfo );
            }
            else
            {
                String userName = userInfo.substring( 0, idx );
                String password = userInfo.substring( idx + 1 );
                uri.setUserName( userName );
                uri.setPassword( password );
            }
        }

        // Now build the root URI
        final StringBuffer rootUri = new StringBuffer();
        appendRootUri( uri, rootUri );
        uri.setContainerUri( rootUri.toString() );

        return uri;
    }
}
