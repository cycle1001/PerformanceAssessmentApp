package net.performance.assessment.interfaces;

/**
 *
 */

public interface CheckPermissionCallback
{
    public void onPermissionAllGranted( );

    public void onPermissionDeniedTemporarily( );

    public void onPermissionDeniedPermantly( );
}
