package voltaprotocol;

import arc.util.Log;
import mindustry.mod.Mod;

import voltaprotocol.content.VPBlocks;
import voltaprotocol.content.VPItems;
import voltaprotocol.content.VPTechTree;

public class VoltaProtocol extends Mod {

    @Override
    public void loadContent(){
        Log.info("[Volta Protocol] Iniciando carga de contenido síncrono...");

        VPItems.load();
        VPBlocks.load();
        VPTechTree.load();

        Log.info("[Volta Protocol] Carga completada de forma segura.");
    }
}