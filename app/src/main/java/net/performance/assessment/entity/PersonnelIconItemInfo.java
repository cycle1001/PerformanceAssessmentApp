package net.performance.assessment.entity;

import android.os.Parcel;

import java.io.Serializable;

/**
 *
 */

public class PersonnelIconItemInfo implements Serializable/*, Parcelable*/
{
    public String id;

    public String name;

    public PersonnelIconItemInfo( String id , String name ){
        this.id = id;
        this.name = name;
    }

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

    private PersonnelIconItemInfo(Parcel in ) {
        this.id = in.readString();
        this.name = in.readString();
    }

    /*@Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.name);
    }

    //required field that makes Parcelables from a Parcel
    public static final Parcelable.Creator<PersonnelIconItemInfo > CREATOR = new Parcelable.Creator<PersonnelIconItemInfo >() {
        public PersonnelIconItemInfo createFromParcel(Parcel in ) {
            return new PersonnelIconItemInfo(in);
        }

        public PersonnelIconItemInfo[] newArray(int size ) {
            return new PersonnelIconItemInfo[size];
        }
    };

    @Override
    public int describeContents( )
    {
        return 0;
    }*/
}
