package net.performance.assessment.entity;

import java.util.List;

/**
 *  登录信息
 */
public class LoginInfo
{

    /**
     {
     "id": "10",
     "isNewRecord": false,
     "remarks": null,
     "createDate": null,
     "updateDate": null,
     "loginName": "jn_jsb",
     "no": null,
     "name": "王八",
     "email": null,
     "phone": null,
     "mobile": null,
     "userType": null,
     "loginIp": null,
     "loginDate": null,
     "loginFlag": "1",
     "photo": null,
     "oldLoginName": null,
     "newPassword": null,
     "oldLoginIp": null,
     "oldLoginDate": null,
     "role": null,
     "appToken": "7fcf52b2801a8212e4779c27a535b64f",
     "tokenDate": null,
     "tokenValide": null,
     "seriesno": "1000013",
     "companyId": null,
     "officeId": "14",
     "companyName": null,
     "companyAreaId": null,
     "companyAreaName": null,
     "officeName": null,
     "officeAreaId": null,
     "officeAreaName": null,
     "oldPassword": null,
     "xingeToken": "42f154d8a490aea547c584dd5eabc16615fceff6",
     "xingeTags": null,
     "appRoleList": [],
     "roleNames": "",
     "admin": false
     }
     */

    public String id;

    public boolean isNewRecord;

    public String loginName;

    public String name;

    public  String loginFlag;

    public String appToken;

    public boolean admin;

    public String roleNames;

    public String seriesno;

    public String remarks;
    public String createDate;
    public String updateDate;
    public String no;
    public String email;
    public String phone;
    public String mobile;
    public String userType;
    public String loginIp;
    public String loginDate;
    public String photo;
    public String oldLoginName;
    public String newPassword;
    public String oldLoginIp;
    public String oldLoginDate;
    public String role;
    public String tokenDate;
    public String tokenValide;
    public String companyId;
    public String officeId;
    public String companyName;
    public String companyAreaId;
    public String companyAreaName;
    public String officeName;
    public String officeAreaId;
    public String officeAreaName;
    public String oldPassword;
    public String xingeToken;
    public String xingeTags;
    public List< ? > appRoleList;

}
