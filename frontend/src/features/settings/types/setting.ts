export interface EmailDomainSetting {
  domain: string;
}

export type PhoneSetting = string[];

export type PhoneSettingRequest = {
  supportedCountryCodes: string[];
};
