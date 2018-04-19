package net.performance.assessment.entity;

/**
 *
 */

public class LoginSimpleInfo
{
    public String account;

    public String password;

    private LoginSimpleInfo( Builder builder )
    {
        this.account =  builder.account;
        this.password = builder.password;
    }

    public String getAccount( )
    {
        return account;
    }

    public String getPassword( )
    {
        return password;
    }


    public static class Builder
    {
        private String account;
        private String password;

        public Builder account( String account )
        {
            this.account = account;
            return this;
        }

        public Builder password( String password )
        {
            this.password = password;
            return this;
        }

        public Builder fromPrototype( LoginSimpleInfo prototype )
        {
            account = prototype.account;
            password = prototype.password;
            return this;
        }

        public LoginSimpleInfo build( )
        {
            return new LoginSimpleInfo( this );
        }
    }
}
