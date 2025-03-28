import EmailDomainSettings from '@/components/settings/EmailDomainSettings';
import PhoneSettings from '@/components/settings/PhoneSettings';
import React from 'react';

const SettingPage: React.FC = () => {
  return (
    <div className='min-h-1/4 gap-4 p-4'>
      <EmailDomainSettings />
      <PhoneSettings />
    </div>
  );
};

export default SettingPage;
