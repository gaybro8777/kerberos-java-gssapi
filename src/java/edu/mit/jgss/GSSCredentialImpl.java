/* 
 * Copyright (C) 2012 by the Massachusetts Institute of Technology.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mit.jgss;

import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSName;

import edu.mit.jgss.swig.*;

public class GSSCredentialImpl implements GSSCredential {
    
    /* Representing our underlying SWIG-wrapped gss_cred_id_t object */
    private gss_cred_id_t_desc internGSSCred;

    public void dispose() throws GSSException {
        
        long[] min_status = {0};
        long ret = 0;

        ret = gsswrapper.gss_release_cred(min_status, this.internGSSCred);

        if (ret != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl(0, (int)min_status[0]);
        }

    }
    
    public GSSName getName() throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};
        int ret = 0;
        gss_name_t_desc name = new gss_name_t_desc();
        gss_OID_set_desc temp_mech_set = new gss_OID_set_desc();
        
        GSSNameImpl tmpName = new GSSNameImpl();
        
        maj_status = gsswrapper.gss_inquire_cred(min_status,
                this.internGSSCred, name, lifetime, cred_usage,
                temp_mech_set);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        ret = tmpName.setInternGSSName(name);
        if (ret != 0) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return tmpName;

    }
    
    public GSSName getName(Oid mechOID) throws GSSException {

        GSSName name = getName();
        GSSName canoniName = name.canonicalize(mechOID);

        return canoniName;
    }
    
    public int getRemainingLifetime() throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();
        gss_OID_set_desc temp_mech_set = new gss_OID_set_desc();
        
        maj_status = gsswrapper.gss_inquire_cred(min_status,
                this.internGSSCred, name, lifetime, cred_usage,
                temp_mech_set);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return (int)lifetime[0];
    }
    
    public int getRemainingInitLifetime(Oid mech) throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};
        long[] init_lifetime = {0};
        long[] accept_lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();

        maj_status = gsswrapper.gss_inquire_cred_by_mech(min_status,
                this.internGSSCred, mech.getNativeOid(), name, init_lifetime,
                accept_lifetime, cred_usage);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return (int)init_lifetime[0];
    }
    
    public int getRemainingAcceptLifetime(Oid mech) throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        long[] init_lifetime = {0};
        long[] accept_lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();

        maj_status = gsswrapper.gss_inquire_cred_by_mech(min_status,
                this.internGSSCred, mech.getNativeOid(), name, init_lifetime,
                accept_lifetime, cred_usage);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return (int)accept_lifetime[0];

    }
    
    public int getUsage() throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();
        gss_OID_set_desc temp_mech_set = new gss_OID_set_desc();
        
        maj_status = gsswrapper.gss_inquire_cred(min_status,
                this.internGSSCred, name, lifetime, cred_usage,
                temp_mech_set);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return cred_usage[0];

    }
    
    public int getUsage(Oid mechOID) throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        long[] init_lifetime = {0};
        long[] accept_lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();

        maj_status = gsswrapper.gss_inquire_cred_by_mech(min_status,
                this.internGSSCred, mechOID.getNativeOid(), name, 
                init_lifetime, accept_lifetime, cred_usage);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return cred_usage[0];

    }
    
    public Oid[] getMechs() throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();
        gss_OID_set_desc temp_mech_set = new gss_OID_set_desc();
        
        maj_status = gsswrapper.gss_inquire_cred(min_status,
                this.internGSSCred, name, lifetime, cred_usage,
                temp_mech_set);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        /* temp_mech_set is a set, retrieve elements using getElement() and
           getCount() */
        Oid[] mechs = new Oid[(int)temp_mech_set.getCount()];

        for (int i = 0; i < temp_mech_set.getCount(); i++) {
            mechs[i] = new Oid(temp_mech_set.getElement(i).toDotString());
        }

        return mechs;
    }
    
    public void add(GSSName aName, int initLifetime, int acceptLifetime,
            Oid mech, int usage) throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};

        maj_status = gsswrapper.gss_add_cred(min_status, this.internGSSCred,
                aName.getInternGSSName(), mech.getNativeOid(),
                usage, initLifetime, acceptLifetime, null, null,
                null, null);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

    }
    
    public boolean equals(Object another) {

        if (! (another instanceof GSSCredential))
            return false;

        GSSCredential tmpCred = (GSSCredential) another;

        if (tmpCred == null)
            throw new NullPointerException("Input Object is null");

        try {

            /* test some elements of our Cred to test for equality */
            if (!tmpCred.getName().equals(this.getName()))
                return false;

            if (tmpCred.getUsage() != this.getUsage())
                return false;

        } catch (GSSException e) {
            return false;
        }

        return false;
    }

    GSSCredential acquireCred(GSSName desiredName, long timeReq,
            Oid[] desiredMechs, int credUsage) throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};
        long[] lifetime = {0};
        int[] cred_usage = {0};
        long[] time_rec = {0};

        gss_name_t_desc dName;
        gss_OID_set_desc dMechs;

        /* handle null GSSName arg */
        if (desiredName != null) {
            dName = desiredName.getInternGSSName();
        } else {
            dName = gsswrapper.GSS_C_NO_NAME;
        }

        /* handle null Oid arg, create gss_OID_set_desc from input set */
        if (desiredMechs != null) {
            dMechs = new gss_OID_set_desc();
            for (int i = 0; i < desiredMechs.length; i++) {
                System.out.println("desiredMechs.length = " + desiredMechs.length);
                maj_status = gsswrapper.gss_add_oid_set_member(min_status,
                        desiredMechs[i].getNativeOid(), dMechs);

                if (maj_status != gsswrapper.GSS_S_COMPLETE) {
                    throw new GSSExceptionImpl((int)maj_status,
                            (int)min_status[0]);
                }
            }
        } else {
            dMechs = gsswrapper.GSS_C_NO_OID_SET;
        }

        /* initialize internal gss_cred_id_t_desc */
        internGSSCred = new gss_cred_id_t_desc();
        
        /* acquire cred */
        maj_status = gsswrapper.gss_acquire_cred(min_status,
                dName, timeReq, dMechs, credUsage, internGSSCred,
                null, time_rec);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return this;

    }

    GSSCredential acquireCred(int usage) throws GSSException {
        
        return acquireCred((GSSName) null, (long) 0, (Oid[]) null, usage);

    }

    GSSCredential acquireCred(GSSName aName, int lifetime, Oid mech,
            int usage) throws GSSException {

        Oid[] mechs = null;

        if (mech != null) {
            mechs = new Oid[1];
            mechs[0] = mech;
        }

        return acquireCred(aName, (long) lifetime, mechs, usage);

    }

    GSSCredential acquireCred(GSSName aName, int lifetime, Oid[] mechs,
            int usage) throws GSSException {

        return acquireCred(aName, (long) lifetime, mechs, usage);

    }

}

