import i18n from '@/shared/i18n/i18n';

export function FormattedDate({ date }: { date: Date }) {
  const locale = i18n.language;
  const formattedDate = new Intl.DateTimeFormat(locale, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(date);

  return <span>{formattedDate}</span>;
}
