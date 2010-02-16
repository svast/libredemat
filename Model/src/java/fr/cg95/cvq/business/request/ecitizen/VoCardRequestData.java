package fr.cg95.cvq.business.request.ecitizen;

import java.io.Serializable;

/**
 * @hibernate.class
 *  table="vo_card_request"
 *  lazy="false"
 *
 * @author bor@zenexity.fr
 */
public class VoCardRequestData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public final void setId(final Long id) {
        this.id = id;
    }

    /**
     * @hibernate.id
     *  column="id"
     *  generator-class="sequence"
     */
    public final Long getId() {
        return this.id;
    }
}
