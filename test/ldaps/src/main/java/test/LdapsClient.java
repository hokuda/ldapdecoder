package test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;


@SuppressWarnings("serial")
public class LdapsClient {

    InitialDirContext dirContext;

    public void doLdaps() throws Exception {
        {
            if (!System.getProperty("javax.net.debug").equals("ssl,record,plaintext")) {
                System.err.println("javax.net.debug=" + System.getProperty("javax.net.debug"));
                System.err.println("set the system property javax.net.debug=ssl,record,plaintext");
                System.exit(1); // act bad
            }
            
            dirContext = getDirContext();
            
            ldapadd();
            
            ldapsearch();

            System.gc(); // needed to send unbind request by unknown reason
            
            ldapmodify();
            
            ldapdnmodify();
            
            ldapdelete();
            
            dirContext.close();
        }

        System.gc(); // needed as well to send unbind request by unknown reason
    }

    public void ldapadd() throws Exception{
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute samaccountname = new BasicAttribute("samaccountname");
            samaccountname.add(0, "tempuser");
            attrs.put(samaccountname);
            
            Attribute objectClass = new BasicAttribute("objectClass");
            objectClass.add(0, "user");
            attrs.put(objectClass);
            
            dirContext.createSubcontext("cn=tempuser,cn=users,dc=example2,dc=com" , attrs );
        }
        catch(Exception e) { e.printStackTrace(); }
    }
        
    public void ldapmodify() throws Exception{
        
        // ADD_ATTRIBUTE
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute sn = new BasicAttribute("sn");
            sn.add(0, "sv");
            attrs.put(sn);
            
            dirContext.modifyAttributes("cn=tempuser,cn=users,dc=example2,dc=com" , DirContext.ADD_ATTRIBUTE, attrs );
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // REPLACE_ATTRIBUTE
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute sn = new BasicAttribute("sn");
            sn.add(0, "sv_replaced");
            attrs.put(sn);
            
            dirContext.modifyAttributes("cn=tempuser,cn=users,dc=example2,dc=com" , DirContext.REPLACE_ATTRIBUTE, attrs );
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // REMOVE_ATTRIBUTE
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute sn = new BasicAttribute("sn");
            sn.add(0, "sv_replaced");
            attrs.put(sn);
            
            dirContext.modifyAttributes("cn=tempuser,cn=users,dc=example2,dc=com" , DirContext.REMOVE_ATTRIBUTE, attrs );
        }
        catch(Exception e) { e.printStackTrace(); }
        
    }
    
    public void ldapdnmodify() throws Exception{
        // ADD_ATTRIBUTE
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute sn = new BasicAttribute("sn");
            sn.add(0, "sv");
            attrs.put(sn);
            
            dirContext.rename("cn=tempuser,cn=users,dc=example2,dc=com" , "cn=new_tempuser,cn=users,dc=WRONG,dc=com");
        }
        catch(Exception e) { e.printStackTrace(); }
        try {
            Attributes attrs = new BasicAttributes();
            
            Attribute sn = new BasicAttribute("sn");
            sn.add(0, "sv");
            attrs.put(sn);
            
            dirContext.rename("cn=tempuser,cn=users,dc=example2,dc=com" , "cn=new_tempuser,cn=users,dc=example2,dc=com");
        }
        catch(Exception e) { e.printStackTrace(); }

    }
    
    public void ldapdelete() throws Exception{
        dirContext.destroySubcontext("cn=tempuser,cn=users,dc=example2,dc=com");
        dirContext.destroySubcontext("cn=new_tempuser,cn=users,dc=example2,dc=com");

    }
    
        
    
    public void ldapsearch() throws NamingException{
        NamingEnumeration<javax.naming.directory.SearchResult> answers = null;

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[] { "entrydn", "uid", "objectClass", "givenName", "sn","memberOf", "title", "createtimestamp", "uidNumber", "cn", "samaccountname", "member", "pwdLastSet" });
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        /*
        AND search filter
        Serve as a container for holding zero or more search filter elements. All search filters contained in the AND filter must match the target entry for the AND filter to match.

        OR search filter
        Serve as a container for holding zero or more search filter elements. At least one of the search filters contained in the OR filter must match the target entry for the OR filter to match.
        
        NOT filters
        Serves as a container for exactly one search filter element. The embedded filter must not match the target entry for the NOT filter to match.
        
        equality search filter
        Provides a mechanism for identifying entries that contain a specified value for a given attribute.
        
        substring search filter
        Provides a mechanism for identifying entries with attribute values matching a specified substring.
        
        greater than or equal to search filter
        Provides a mechanism for identifying entries with attribute values greater than or equal to a specific value.
        
        less than or equal to search filter
        Provides a mechanism for identifying entries with attribute values less than or equal to a specific value.
        
        presence search filter
        Provides a mechanism for identifying entries that contain at least one value for a specified attribute.
        
        approximate search filter
        Provides a mechanism for identifying entries with attribute values that are approximately equal to a given value.
        
        extensible match search filter
        Provides a mechanism for using a matching rule to identify matching entries using an extensible mechanism.
        
        See RFC 4515 for more information about LDAP search filters and a mechanism for representing them as strings.
        */

        /* test for ldapsearch with wrong base dn */
        try {
            //NamingEnumeration<javax.naming.directory.SearchResult> answers = dirContext.search("dc=redhat,dc=com", "(&(uid=*)(|(!(uid=nosuchuser))(sn:dn:2.4.6.8.10:=hoge)(createTimestamp>=20070101000000Z)(createTimestamp=<21000101000000Z)(cn~=ser)))", ctrls);
            NamingEnumeration<javax.naming.directory.SearchResult> _answers = dirContext.search("dc=redhat,dc=com", "(&(uid=*)(|(!(uid=nosuchuser))(sn:dn:2.16.840.1.113730.3.3.2.7.1:=passin)(createTimestamp>=20070101000000Z)(createTimestamp<=21000101000000Z)(cn=u*)(cn=*u)(cn=*u*)(cn=u*s*)(cn~=ser)))", ctrls);
            answers = _answers;
            while (answers.hasMore()) {
                //System.out.println(answers.next());
            }
        }
        catch(Exception e) {e.printStackTrace();}
        finally { if (answers != null)  answers.close(); }

        /* test for ldapsearch with valid base dn */
        try {
            //NamingEnumeration<javax.naming.directory.SearchResult> answers = dirContext.search("dc=redhat,dc=com", "(&(uid=*)(|(!(uid=nosuchuser))(sn:dn:2.4.6.8.10:=hoge)(createTimestamp>=20070101000000Z)(createTimestamp=<21000101000000Z)(cn~=ser)))", ctrls);
            NamingEnumeration<javax.naming.directory.SearchResult> _answers = dirContext.search("dc=example2,dc=com", "(&(samaccountname=*)(|(!(uid=nosuchuser))(sn:dn:2.16.840.1.113730.3.3.2.7.1:=passin)(createTimestamp>=20070101000000Z)(createTimestamp<=21000101000000Z)(cn=u*)(cn=*u)(cn=*u*)(cn=u*s*)(cn~=ser)))", ctrls);
            answers = _answers;
            while (answers.hasMore()) {
                //System.out.println(answers.next());
                answers.next();
            }
            answers.close();
        }
        catch(Exception e) {e.printStackTrace();}
        finally { if (answers != null)  answers.close(); }
    }

    public InitialDirContext getDirContext() throws NamingException{
        // fail intentionally
        try {
            /* test for bind with a wrong password */
            Properties props1 = new Properties();
            props1.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props1.put(Context.PROVIDER_URL, "ldaps://ad2016:636");
            props1.put(Context.SECURITY_PRINCIPAL, "Administrator@EXAMPLE2.COM");
            props1.put(Context.SECURITY_CREDENTIALS, "WrongPassw0rd.");
            props1.put(Context.REFERRAL, "ignore");
            
            InitialDirContext context1 = new InitialDirContext(props1);
        }
        catch(Exception e) {e.printStackTrace();}

        // should work
        /* test for bind with a wrong password */
        Properties props2 = new Properties();
        props2.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props2.put(Context.PROVIDER_URL, "ldaps://ad2016:636");
        props2.put(Context.SECURITY_PRINCIPAL, "Administrator@EXAMPLE2.COM");
        props2.put(Context.SECURITY_CREDENTIALS, "Passw0rd.");
        //props2.put(Context.REFERRAL, "ignore");
        props2.put(Context.REFERRAL, "follow");
        props2.put("java.naming.ldap.factory.socket", "test.LazySSLSocketFactory");

        
        InitialDirContext context2 = new InitialDirContext(props2);
        return context2;
    }
}
