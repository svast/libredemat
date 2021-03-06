
package org.libredemat.business.request.school;

import java.io.Serializable;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalTime;

import net.sf.oval.constraint.*;
import org.apache.xmlbeans.XmlOptions;
import org.libredemat.business.authority.*;
import org.libredemat.business.request.*;
import org.libredemat.business.users.*;
import org.libredemat.xml.common.RequestType;
import org.libredemat.xml.common.*;
import org.libredemat.xml.request.school.*;
import org.libredemat.service.request.LocalReferential;
import org.libredemat.service.request.condition.IConditionChecker;
import javax.persistence.*;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

/**
 * Generated class file, do not edit !
 */
@Entity
@Table(name="perischool_contact_individual")
public class PerischoolContactIndividual implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Map<String, IConditionChecker> conditions =
        PerischoolActivityRegistrationRequest.conditions;

    public PerischoolContactIndividual() {
        super();
      
    }

    public final String modelToXmlString() {
        PerischoolContactIndividualType object = (PerischoolContactIndividualType) this.modelToXml();
        XmlOptions opts = new XmlOptions();
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(4);
        opts.setUseDefaultNamespace();
        opts.setCharacterEncoding("UTF-8");
        return object.xmlText(opts);
    }

    public final PerischoolContactIndividualType modelToXml() {
        
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        LocalTime localTime = new LocalTime();
        PerischoolContactIndividualType perischoolContactIndividual = PerischoolContactIndividualType.Factory.newInstance();
        int i = 0;
    
        perischoolContactIndividual.setOfficePhone(this.officePhone);
      
        if (this.address != null)
            perischoolContactIndividual.setAddress(Address.modelToXml(this.address));
      
        perischoolContactIndividual.setFirstName(this.firstName);
      
        perischoolContactIndividual.setLastName(this.lastName);
      
        perischoolContactIndividual.setHomePhone(this.homePhone);
      
        return perischoolContactIndividual;
    }

    public static PerischoolContactIndividual xmlToModel(PerischoolContactIndividualType perischoolContactIndividualDoc) {
        Calendar calendar = Calendar.getInstance();
        LocalTime localTime = new LocalTime();
        List list = new ArrayList();
        PerischoolContactIndividual perischoolContactIndividual = new PerischoolContactIndividual();
    
        perischoolContactIndividual.setOfficePhone(perischoolContactIndividualDoc.getOfficePhone());
      
        if (perischoolContactIndividualDoc.getAddress() != null)
            perischoolContactIndividual.setAddress(Address.xmlToModel(perischoolContactIndividualDoc.getAddress()));
      
        perischoolContactIndividual.setFirstName(perischoolContactIndividualDoc.getFirstName());
      
        perischoolContactIndividual.setLastName(perischoolContactIndividualDoc.getLastName());
      
        perischoolContactIndividual.setHomePhone(perischoolContactIndividualDoc.getHomePhone());
      
        return perischoolContactIndividual;
    }

    @Override
    public PerischoolContactIndividual clone() {
        PerischoolContactIndividual result = new PerischoolContactIndividual();
        
          
            
        result.setOfficePhone(officePhone);
      
          
        
          
            
        if (address != null)
            result.setAddress(address.clone());
      
          
        
          
            
        result.setFirstName(firstName);
      
          
        
          
            
        result.setLastName(lastName);
      
          
        
          
            
        result.setHomePhone(homePhone);
      
          
        
        return result;
    }

    private Long id;

    public final void setId(final Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    public final Long getId() {
        return this.id;
    }

  
    
      @MaxLength(
        
          value = 10,
        
        
        profiles = {"contact"},
        message = "officePhone"
      )
    
    private String officePhone;

    public void setOfficePhone(final String officePhone) {
        this.officePhone = officePhone;
    }


    @Column(name="office_phone" , length=10 )
      
    public String getOfficePhone() {
        return this.officePhone;
    }
  
    
      @NotNull(
        
        
        profiles = {"contact"},
        message = "address"
      )
    
      @AssertValid(
        
        
        profiles = {"contact"},
        message = "address"
      )
    
    private org.libredemat.business.users.Address address;

    public void setAddress(final org.libredemat.business.users.Address address) {
        this.address = address;
    }


    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="address_id")
      
    public org.libredemat.business.users.Address getAddress() {
        return this.address;
    }
  
    
      @MaxLength(
        
          value = 38,
        
        
        profiles = {"contact"},
        message = "firstName"
      )
    
      @NotNull(
        
        
        profiles = {"contact"},
        message = "firstName"
      )
    
      @NotBlank(
        
        
        profiles = {"contact"},
        message = "firstName"
      )
    
    private String firstName;

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }


    @Column(name="first_name" , length=38 )
      
    public String getFirstName() {
        return this.firstName;
    }
  
    
      @MaxLength(
        
          value = 38,
        
        
        profiles = {"contact"},
        message = "lastName"
      )
    
      @NotNull(
        
        
        profiles = {"contact"},
        message = "lastName"
      )
    
      @NotBlank(
        
        
        profiles = {"contact"},
        message = "lastName"
      )
    
    private String lastName;

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }


    @Column(name="last_name" , length=38 )
      
    public String getLastName() {
        return this.lastName;
    }
  
    
      @MaxLength(
        
          value = 10,
        
        
        profiles = {"contact"},
        message = "homePhone"
      )
    
    private String homePhone;

    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }


    @Column(name="home_phone" , length=10 )
      
    public String getHomePhone() {
        return this.homePhone;
    }
  
}
