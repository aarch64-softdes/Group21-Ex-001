import { AppSidebar } from '@/components/layouts/AppSidebar';
import { SidebarProvider } from '@/components/ui/sidebar';

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <SidebarProvider
      style={{
        '--sidebar-width': '200px',
      }}
    >
      <AppSidebar />
      <main>{children}</main>
    </SidebarProvider>
  );
}
