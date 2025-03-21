interface BaseIdentityDocument {
  documentNumber: string;
  issueDate: string;
  issuePlace: string;
  expiryDate: string;
}

interface CMND extends BaseIdentityDocument {
  type: "CMND";
}

interface CCCD extends BaseIdentityDocument {
  type: "CCCD";
  hasChip: boolean;
}

interface Passport extends BaseIdentityDocument {
  type: "Passport";
  issueCountry: string;
}

export type IdentityDocument = CMND | CCCD | Passport;
