import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import * as XLSX from 'xlsx';

/**
 * Servicio para exportación de datos a Excel, PDF y CSV
 * Proporciona métodos consistentes para exportar información
 */
@Injectable({
  providedIn: 'root'
})
export class ExportService {

  constructor() { }

  /**
   * Exporta datos a Excel
   */
  exportToExcel<T>(
    data: T[],
    filename: string = 'export',
    sheetName: string = 'Datos',
    columns?: Array<{key: keyof T, label: string}>
  ): void {
    try {
      // Preparar datos para Excel
      const excelData = this.prepareDataForExport(data, columns);

      // Crear workbook
      const wb: XLSX.WorkBook = XLSX.utils.book_new();

      // Crear worksheet
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(excelData);

      // Configurar ancho de columnas
      const colWidths = this.calculateColumnWidths(excelData);
      ws['!cols'] = colWidths;

      // Agregar worksheet al workbook
      XLSX.utils.book_append_sheet(wb, ws, sheetName);

      // Guardar archivo
      const fileName = `${filename}_${this.getTimestamp()}.xlsx`;
      XLSX.writeFile(wb, fileName);

    } catch (error) {
      console.error('Error al exportar a Excel:', error);
      throw new Error('Error al generar archivo Excel');
    }
  }

  /**
   * Exporta datos a CSV
   */
  exportToCSV<T>(
    data: T[],
    filename: string = 'export',
    columns?: Array<{key: keyof T, label: string}>,
    delimiter: string = ','
  ): void {
    try {
      // Preparar datos
      const csvData = this.prepareDataForExport(data, columns);

      // Generar CSV
      const csv = this.convertToCSV(csvData, delimiter);

      // Descargar archivo
      const fileName = `${filename}_${this.getTimestamp()}.csv`;
      this.downloadFile(csv, fileName, 'text/csv;charset=utf-8;');

    } catch (error) {
      console.error('Error al exportar a CSV:', error);
      throw new Error('Error al generar archivo CSV');
    }
  }

  /**
   * Exporta datos a PDF (requiere jsPDF)
   */
  exportToPDF<T>(
    data: T[],
    filename: string = 'export',
    title: string = 'Reporte',
    columns?: Array<{key: keyof T, label: string}>
  ): Observable<void> {
    // Por ahora retornamos Observable vacío
    // TODO: Implementar cuando se instale jsPDF
    console.warn('Exportación a PDF pendiente de implementar');
    return of();
  }

  /**
   * Exporta múltiples hojas a Excel
   */
  exportMultiSheetExcel<T>(
    sheets: Array<{
      data: T[];
      name: string;
      columns?: Array<{key: keyof T, label: string}>;
    }>,
    filename: string = 'export_multi'
  ): void {
    try {
      const wb: XLSX.WorkBook = XLSX.utils.book_new();

      sheets.forEach(sheet => {
        // Preparar datos
        const excelData = this.prepareDataForExport(sheet.data, sheet.columns);

        // Crear worksheet
        const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(excelData);

        // Configurar columnas
        const colWidths = this.calculateColumnWidths(excelData);
        ws['!cols'] = colWidths;

        // Agregar al workbook
        XLSX.utils.book_append_sheet(wb, ws, sheet.name);
      });

      // Guardar archivo
      const fileName = `${filename}_${this.getTimestamp()}.xlsx`;
      XLSX.writeFile(wb, fileName);

    } catch (error) {
      console.error('Error al exportar Excel multi-hoja:', error);
      throw new Error('Error al generar archivo Excel');
    }
  }

  /**
   * Prepara datos para exportación aplicando configuración de columnas
   */
  private prepareDataForExport<T>(
    data: T[],
    columns?: Array<{key: keyof T, label: string}>
  ): any[] {
    if (!data || data.length === 0) {
      return [];
    }

    // Si no se especifican columnas, usar todas las propiedades
    if (!columns) {
      return data.map(item => ({ ...item }));
    }

    // Mapear datos según columnas especificadas
    return data.map(item => {
      const mappedItem: any = {};
      columns.forEach(col => {
        mappedItem[col.label] = this.formatCellValue(item[col.key]);
      });
      return mappedItem;
    });
  }

  /**
   * Formatea valores de celda para exportación
   */
  private formatCellValue(value: any): any {
    if (value === null || value === undefined) {
      return '';
    }

    // Fechas
    if (value instanceof Date) {
      return value.toLocaleDateString('es-ES');
    }

    // Strings de fecha ISO
    if (typeof value === 'string' && this.isISODateString(value)) {
      return new Date(value).toLocaleDateString('es-ES');
    }

    // Booleanos
    if (typeof value === 'boolean') {
      return value ? 'Sí' : 'No';
    }

    // Números
    if (typeof value === 'number') {
      return value;
    }

    // Arrays y objetos
    if (typeof value === 'object') {
      return JSON.stringify(value);
    }

    return value.toString();
  }

  /**
   * Convierte datos a formato CSV
   */
  private convertToCSV(data: any[], delimiter: string = ','): string {
    if (!data || data.length === 0) {
      return '';
    }

    // Headers
    const headers = Object.keys(data[0]);
    const csvHeaders = headers.join(delimiter);

    // Rows
    const csvRows = data.map(row =>
      headers.map(header => {
        const value = row[header];
        // Escapar comillas y envolver en comillas si contiene delimiter
        const stringValue = value?.toString() || '';
        const escapedValue = stringValue.replace(/"/g, '""');
        return stringValue.includes(delimiter) || stringValue.includes('"') || stringValue.includes('\n')
          ? `"${escapedValue}"`
          : escapedValue;
      }).join(delimiter)
    );

    return [csvHeaders, ...csvRows].join('\n');
  }

  /**
   * Calcula anchos de columna para Excel
   */
  private calculateColumnWidths(data: any[]): Array<{wch: number}> {
    if (!data || data.length === 0) {
      return [];
    }

    const headers = Object.keys(data[0]);
    const colWidths: Array<{wch: number}> = [];

    headers.forEach((header, index) => {
      // Ancho basado en el header
      let maxWidth = header.length;

      // Revisar contenido de las primeras 100 filas para optimizar
      const sampleSize = Math.min(data.length, 100);
      for (let i = 0; i < sampleSize; i++) {
        const cellValue = data[i][header]?.toString() || '';
        maxWidth = Math.max(maxWidth, cellValue.length);
      }

      // Limitar ancho entre 10 y 50 caracteres
      const width = Math.max(10, Math.min(50, maxWidth + 2));
      colWidths.push({ wch: width });
    });

    return colWidths;
  }

  /**
   * Descarga archivo desde el navegador
   */
  private downloadFile(content: string, filename: string, contentType: string): void {
    const blob = new Blob(['\ufeff' + content], { type: contentType });
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.style.display = 'none';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    window.URL.revokeObjectURL(url);
  }

  /**
   * Genera timestamp para nombres de archivo
   */
  private getTimestamp(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    return `${year}${month}${day}_${hours}${minutes}`;
  }

  /**
   * Verifica si un string es una fecha ISO
   */
  private isISODateString(value: string): boolean {
    const isoDateRegex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d{3})?Z?$/;
    return isoDateRegex.test(value);
  }

  /**
   * Configuración de exportación para diferentes tipos de reporte
   */
  getReportConfig(reportType: 'users' | 'products' | 'orders' | 'categories'): any {
    const configs = {
      users: {
        filename: 'usuarios',
        sheetName: 'Usuarios',
        title: 'Reporte de Usuarios'
      },
      products: {
        filename: 'productos',
        sheetName: 'Productos',
        title: 'Reporte de Productos'
      },
      orders: {
        filename: 'pedidos',
        sheetName: 'Pedidos',
        title: 'Reporte de Pedidos'
      },
      categories: {
        filename: 'categorias',
        sheetName: 'Categorías',
        title: 'Reporte de Categorías'
      }
    };

    return configs[reportType] || configs.users;
  }

  /**
   * Exporta con configuración automática según tipo
   */
  exportByType<T>(
    data: T[],
    reportType: 'users' | 'products' | 'orders' | 'categories',
    format: 'excel' | 'csv' = 'excel',
    columns?: Array<{key: keyof T, label: string}>
  ): void {
    const config = this.getReportConfig(reportType);

    if (format === 'excel') {
      this.exportToExcel(data, config.filename, config.sheetName, columns);
    } else {
      this.exportToCSV(data, config.filename, columns);
    }
  }
}
