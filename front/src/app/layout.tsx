import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import QueryProvider from "@/providers/query-provider";
import Sidebar from "@/components/layout/sidebar";
import Header from "@/components/layout/header";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "FinSight - 스마트 주식 분석",
  description: "데이터 기반 주식 분석 및 시장 인사이트 플랫폼",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" className="dark">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <QueryProvider>
          <div className="flex min-h-screen">
            <Sidebar />
            <div className="ml-60 flex flex-1 flex-col">
              <Header />
              <main className="flex-1 p-6">{children}</main>
            </div>
          </div>
        </QueryProvider>
      </body>
    </html>
  );
}
