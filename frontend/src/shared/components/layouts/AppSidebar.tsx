import {
  Activity,
  Bookmark,
  FileText,
  GraduationCap,
  Library,
  Settings,
  Users,
} from 'lucide-react';

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSubItem,
} from '@ui/sidebar';
import { useTranslation } from 'react-i18next';
import { LanguageSwitcher } from '../common/LanguageSwitcher';

export function AppSidebar() {
  const { t } = useTranslation('common');

  // Menu items.
  const items = [
    {
      title: t('students'),
      url: 'student',
      icon: GraduationCap,
    },
    {
      title: t('faculties'),
      url: 'faculty',
      icon: Users,
    },
    {
      title: t('programs'),
      url: 'program',
      icon: Library,
    },
    {
      title: t('status'),
      url: 'status',
      icon: Activity,
    },
    {
      title: t('subjects'),
      url: 'subject',
      icon: Bookmark,
    },
    {
      title: t('courses'),
      url: 'course',
      icon: FileText,
    },
    {
      title: t('settings'),
      url: 'setting',
      icon: Settings,
    },
  ];

  return (
    <Sidebar
      style={{
        '--sidebar-width': '240px',
      }}
    >
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>{t('appName')}</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton
                    asChild
                    isActive={window.location.pathname == `/${item.url}`}
                  >
                    <a href={item.url}>
                      <item.icon />
                      <span>{item.title}</span>
                    </a>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter className='p-4'>
        <LanguageSwitcher />
      </SidebarFooter>
    </Sidebar>
  );
}
