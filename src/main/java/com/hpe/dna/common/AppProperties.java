package com.hpe.dna.common;

import com.google.common.base.MoreObjects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *@author zhi-yong.zhu@hpe.com on 12/19/2015.
 */
@Configuration
@PropertySource("classpath:app.properties")
public class AppProperties {
    @Value("${app.base.uri}")
    private String appBaseUri;

    @Value("${token.expiration.days}")
    private long tokenExpirationDays;

    @Value("${identityProvider}")
    private String identityProvider;

    @Value("${mongo.connection.string}")
    private String mongoConnectionString;

    @Value("${mongo.db.name}")
    private String mongoDbName;

    @Value("${app.data.setup.passcode}")
    private String appDataSetupPasscode;

    @Value("${email.registration.activate.subject}")
    private String emailRegistrationActivateSubject;

    @Value("${email.registration.approve.subject}")
    private String emailRegistrationApproveEmailSubject;

    @Value("${email.password.reset.request.subject}")
    private String emailPasswordResetRequestSubject;

    @Value("${email.password.reset.successfully.subject}")
    private String emailPasswordResetSuccessfullySubject;

    @Value("${registration.activation.code.expired.in.hours}")
    private long activationCodeExpired;

    @Value("${email.approved.subject}")
    private String emailApprovedSubject;

    @Value("${dna.admin.email}")
    private String dnaAdminEmail;

    public String getAppBaseUri() {
        return appBaseUri;
    }

    public void setAppBaseUri(String appBaseUri) {
        this.appBaseUri = appBaseUri;
    }


    public long getTokenExpirationDays() {
        return tokenExpirationDays;
    }

    public void setTokenExpirationDays(long tokenExpirationDays) {
        this.tokenExpirationDays = tokenExpirationDays;
    }

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }

    public String getMongoConnectionString() {
        return mongoConnectionString;
    }

    public void setMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }

    public String getAppDataSetupPasscode() {
        return appDataSetupPasscode;
    }

    public void setAppDataSetupPasscode(String appDataSetupPasscode) {
        this.appDataSetupPasscode = appDataSetupPasscode;
    }

    public String getEmailRegistrationActivateSubject() {
        return emailRegistrationActivateSubject;
    }

    public void setEmailRegistrationActivateSubject(String emailRegistrationActivateSubject) {
        this.emailRegistrationActivateSubject = emailRegistrationActivateSubject;
    }


    public String getEmailRegistrationApproveSubject() {
        return emailRegistrationApproveEmailSubject;
    }

    public void setEmailRegistrationApproveEmailSubject(String emailRegistrationApproveEmailSubject) {
        this.emailRegistrationApproveEmailSubject = emailRegistrationApproveEmailSubject;
    }

    public long getActivationCodeExpired() {
        return activationCodeExpired;
    }

    public void setActivationCodeExpired(long activationCodeExpired) {
        this.activationCodeExpired = activationCodeExpired;
    }

    public String getEmailApprovedSubject() {
        return emailApprovedSubject;
    }

    public void setEmailApprovedSubject(String emailApprovedSubject) {
        this.emailApprovedSubject = emailApprovedSubject;
    }

    public String getDnaAdminEmail() {
        return dnaAdminEmail;
    }

    public void setDnaAdminEmail(String dnaAdminEmail) {
        this.dnaAdminEmail = dnaAdminEmail;
    }

    public String getEmailPasswordResetRequestSubject() {
        return emailPasswordResetRequestSubject;
    }

    public void setEmailPasswordResetRequestSubject(String emailPasswordResetRequestSubject) {
        this.emailPasswordResetRequestSubject = emailPasswordResetRequestSubject;
    }

    public String getEmailPasswordResetSuccessfullySubject() {
        return emailPasswordResetSuccessfullySubject;
    }

    public void setEmailPasswordResetSuccessfullySubject(String emailPasswordResetSuccessfullySubject) {
        this.emailPasswordResetSuccessfullySubject = emailPasswordResetSuccessfullySubject;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appBaseUri", appBaseUri)
                .add("tokenExpirationDays", tokenExpirationDays)
                .add("identityProvider", identityProvider)
                .add("mongoConnectionString", mongoConnectionString)
                .add("mongoDbName", mongoDbName)
                .add("appDataSetupPasscode", appDataSetupPasscode)
                .add("emailRegistrationActivateSubject", emailRegistrationActivateSubject)
                .add("emailRegistrationApproveEmailSubject", emailRegistrationApproveEmailSubject)
                .add("emailPasswordResetRequestSubject", emailPasswordResetRequestSubject)
                .add("emailPasswordResetSuccessfullySubject", emailPasswordResetSuccessfullySubject)
                .add("activationCodeExpired", activationCodeExpired)
                .add("emailApprovedSubject", emailApprovedSubject)
                .add("dnaAdminEmail", dnaAdminEmail)
                .toString();
    }
}
