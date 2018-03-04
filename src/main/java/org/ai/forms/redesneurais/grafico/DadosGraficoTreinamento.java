package org.ai.forms.redesneurais.grafico;

import java.text.DecimalFormat;
import java.util.List;

import org.ai.bo.neural.treino.EstatisticaTreinamento;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Edison
 */
public class DadosGraficoTreinamento {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private XYSeries seriesTaxaAprendizado;
    private XYSeries seriesErroQuadratico;
    private XYSeriesCollection datasetTaxaAprendizado;
    private XYSeriesCollection datasetErroQuadratico;
    private JFreeChart chart;
    private DecimalFormat formatter = new DecimalFormat("##0.0000000000");
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    

    public JFreeChart getChart() {
        return chart;
    }

    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public DadosGraficoTreinamento() {
        seriesErroQuadratico = new XYSeries("Erro Quadrático Médio");        
        seriesTaxaAprendizado = new XYSeries("Taxa de Aprendizado");   
        datasetErroQuadratico = new XYSeriesCollection(seriesErroQuadratico);
        datasetTaxaAprendizado = new XYSeriesCollection(seriesTaxaAprendizado);
        init();
    }
    
    public DadosGraficoTreinamento(List<EstatisticaTreinamento> estatisticas) {
        seriesErroQuadratico = new XYSeries("Erro Quadrático Médio");        
        seriesTaxaAprendizado = new XYSeries("Taxa de Aprendizado");   
        for (EstatisticaTreinamento estatistica : estatisticas) {
            seriesErroQuadratico.add(estatistica.getEpoca(),estatistica.getErroQuadraticoMedio());
            seriesTaxaAprendizado.add(estatistica.getEpoca(),estatistica.getTaxaAprendizagem());
        }
        datasetErroQuadratico = new XYSeriesCollection(seriesErroQuadratico);
        datasetTaxaAprendizado = new XYSeriesCollection(seriesTaxaAprendizado);
        init();
    }
    
    
    
   
    
    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
     private void init(){
        
        XYItemRenderer rendererErroQuadratico = new StandardXYItemRenderer();
        rendererErroQuadratico.setBaseToolTipGenerator(
                new XYToolTipGenerator() {
            public String generateToolTip(XYDataset dataset, int serie, int item) {
                 return "Época: " + (int)dataset.getXValue(serie, item) + " - Erro Quadrático: " + formatter.format(dataset.getYValue(serie, item)) ;
            }
        });


        XYPlot plotErroQuadratico = new XYPlot(datasetErroQuadratico, null, new NumberAxis("Erro"), rendererErroQuadratico);
        plotErroQuadratico.setOrientation(PlotOrientation.VERTICAL);

        
        
        XYItemRenderer rendererTaxaAprendizagem = new StandardXYItemRenderer();
        rendererTaxaAprendizagem.setBaseToolTipGenerator(
                new XYToolTipGenerator() {
            public String generateToolTip(XYDataset dataset, int serie, int item) {
                 return "Época: " + (int)dataset.getXValue(serie, item) + " - Taxa de Aprendizagem: " + formatter.format(dataset.getYValue(serie, item)) ;
            }
        });
        
     
        XYPlot plotTaxaAprendizagem = new XYPlot(datasetTaxaAprendizado, null, new NumberAxis("Taxa"), rendererTaxaAprendizagem);
        plotTaxaAprendizagem.setOrientation(PlotOrientation.VERTICAL);
        
       
        ValueAxis rangeAxis = new NumberAxis("Época");
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot();
        combinedPlot.setDomainAxis(rangeAxis);
        
        combinedPlot.setOrientation(PlotOrientation.VERTICAL);
        combinedPlot.add(plotErroQuadratico, 5);
        combinedPlot.add(plotTaxaAprendizagem, 1);

        chart = new JFreeChart("Grafico de Aprendizado da Rede Neural",null, combinedPlot,false);
        
        
//        chart = ChartFactory.createXYLineChart(
//                "Aprendizado da Rede Neural", // title
//                "Épocas", // x-axis label
//                "Erro Quadrático Médio", // y-axis label
//                dataset, // data
//                PlotOrientation.VERTICAL,
//                false, // create legend?
//                false, // generate tooltips?
//                false // generate URLs?
//                );
         
        
    }
    
    public void clear(){
        seriesErroQuadratico.clear();
        seriesTaxaAprendizado.clear();
    }
    
    public void addValue(EstatisticaTreinamento dados){
        seriesErroQuadratico.add(dados.getEpoca(),dados.getErroQuadraticoMedio(),false);
        seriesTaxaAprendizado.add(dados.getEpoca(),dados.getTaxaAprendizagem(),false);
    }

    public void updateChart(){
        
        seriesErroQuadratico.fireSeriesChanged();
        seriesTaxaAprendizado.fireSeriesChanged();
    }

    
    // </editor-fold>
    
}
