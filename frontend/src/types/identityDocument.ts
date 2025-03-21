interface IdentityDocument {
  type: 'Identity Card' | 'Chip Card' | 'Passport';
  number: string;
  issuedDate: Date;
  expiryDate: Date;
  issuedBy: string;

  hasChip?: boolean;

  country?: string;
  notes?: string;
}

export default IdentityDocument;
