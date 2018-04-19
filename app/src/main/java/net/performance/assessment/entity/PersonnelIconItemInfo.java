package net.performance.assessment.entity;

import java.io.Serializable;

/**
 *
 */

public class PersonnelIconItemInfo implements Serializable
{
    public String id;

    public String name;

    private PersonnelIconItemInfo( Builder builder )
    {
        this.id = builder.id;
        this.name = builder.name;
    }


    public static class Builder
    {
        private String id;
        private String name;

        public Builder id( String id )
        {
            this.id = id;
            return this;
        }

        public Builder name( String name )
        {
            this.name = name;
            return this;
        }

        public Builder fromPrototype( PersonnelIconItemInfo prototype )
        {
            id = prototype.id;
            name = prototype.name;
            return this;
        }

        public PersonnelIconItemInfo build( )
        {
            return new PersonnelIconItemInfo( this );
        }
    }
}
