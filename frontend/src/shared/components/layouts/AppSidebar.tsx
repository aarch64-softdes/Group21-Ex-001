import {
  Activity,
  Bookmark,
  ClipboardList,
  FileText,
  GraduationCap,
  Library,
  Settings,
  Users,
} from 'lucide-react';

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSubItem,
} from '@ui/sidebar';

// Menu items.
const items = [
  {
    title: 'Student',
    url: 'student',
    icon: GraduationCap,
  },
  {
    title: 'Faculty',
    url: 'faculty',
    icon: Users,
  },
  {
    title: 'Program',
    url: 'program',
    icon: Library,
  },
  {
    title: 'Status',
    url: 'status',
    icon: Activity,
  },
  {
    title: 'Subject',
    url: 'subject',
    icon: Bookmark,
  },
  {
    title: 'Course',
    url: 'course',
    icon: FileText,
  },
  {
    title: 'Setting',
    url: 'setting',
    icon: Settings,
  },
];

export function AppSidebar() {
  return (
    <Sidebar
      style={{
        '--sidebar-width': '240px',
      }}
    >
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Application</SidebarGroupLabel>
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
    </Sidebar>
  );
}
