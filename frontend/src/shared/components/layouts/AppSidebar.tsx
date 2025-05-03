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
} from '@ui/sidebar';
import { t } from 'i18next';
import { LanguageSwitcher } from '../common/LanguageSwitcher';

export function AppSidebar() {
  // Menu items.
  const items = [
    {
      title: t('common:students'),
      url: 'student',
      icon: GraduationCap,
    },
    {
      title: t('common:faculties'),
      url: 'faculty',
      icon: Users,
    },
    {
      title: t('common:programs'),
      url: 'program',
      icon: Library,
    },
    {
      title: t('common:status'),
      url: 'status',
      icon: Activity,
    },
    {
      title: t('common:subjects'),
      url: 'subject',
      icon: Bookmark,
    },
    {
      title: t('common:courses'),
      url: 'course',
      icon: FileText,
    },
    {
      title: t('common:settings'),
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
          <SidebarGroupLabel>{t('common:appName')}</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
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
