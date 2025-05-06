import { useTranslation } from 'react-i18next';
import { Button } from '@ui/button';
import { Check, Globe } from 'lucide-react';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@ui/dropdown-menu';

const languages = [
  { code: 'en', label: 'English' },
  { code: 'vi', label: 'Tiếng Việt' },
];

export function LanguageSwitcher() {
  const { i18n } = useTranslation();

  const toggleLanguage = (code: string) => {
    i18n.changeLanguage(code);
    window.location.reload();
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant='ghost' size='sm' className='justify-start w-fit'>
          <Globe className='h-4 w-4' />
          <span>{i18n.language === 'en' ? 'English' : 'Tiếng Việt'}</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end'>
        {languages.map((lang) => (
          <DropdownMenuItem
            key={lang.code}
            onClick={() => toggleLanguage(lang.code)}
            className='flex items-center justify-between'
          >
            {lang.label}
            {i18n.language === lang.code && <Check className='ml-2 h-4 w-4' />}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
