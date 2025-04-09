import EmailDomainSettings from '@settings/components/EmailDomainSettings';
import PhoneSettings from '@settings/components/PhoneSettings';
import React from 'react';

const SettingPage: React.FC = () => {
  return (
    <div className='grid min-h-1/4 gap-4 p-4'>
      <EmailDomainSettings />
      <PhoneSettings />
    </div>
  );
};

export default SettingPage;
