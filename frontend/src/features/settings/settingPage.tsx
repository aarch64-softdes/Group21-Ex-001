import EmailDomainSettings from '@settings/components/EmailDomainSettings';
import PhoneSettings from '@settings/components/PhoneSettings';
import React from 'react';
import AdjustmentDurationSettings from './components/AdjustmentDurationSetting';

const SettingPage: React.FC = () => {
  return (
    <div className='min-h-1/4 gap-4 p-4'>
      <div className='flex flex-col gap-4 items-center justify-between pb-2'>
        <h1 className='text-2xl font-bold'>Settings</h1>
      </div>

      <div className='space-y-4'>
        <EmailDomainSettings />
        <PhoneSettings />
        <AdjustmentDurationSettings />
      </div>
    </div>
  );
};

export default SettingPage;
