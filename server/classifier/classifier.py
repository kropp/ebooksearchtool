''' classificator for tags '''

import re
import math

def getwords(doc):
    splitter = re.compile('\\W*')
    # split under non-literals
    words = [s.lower() for s in splitter.split(doc)
        if len(s) > 2 and len(s) < 20]
        
    #return array of unic words
    return dict([ (w,1) for w in words])
    
class classifier:
    def __init__(self, getfeatures, filename=None):
        #counter for feature/category
        self.fc = {}
        # document per category counter
        self.cc = {}
        # function that gets feature from document ( 'getwords' now )
        self.getfeatures = getfeatures
        
    def incf(self, f, cat):
        ''' increments self.fc '''
        
        self.fc.setdefault(f, {})
        self.fc[f].setdefault(cat, 0)
        self.fc[f][cat] += 1
        
    def incc(self, cat):
        ''' increments self.cc '''
        self.cc.setdefault(cat, 0)
        self.cc[cat] += 1
        
    def fcount(self, f, cat):
        ''' counts feature in current category '''
        if f in self.fc and cat in self.fc[f]:
            return float(self.fc[f][cat])
        return 0.0
        
    def catcount(self, cat):
        ''' counts docs for category '''
        if cat in self.cc:
            return float(self.cc[cat])
        return 0
    
    def totalcount(self):
        ''' total docs count '''
        return sum(self.cc.values())
        
    def categories(self):
        ''' total category count '''
        return self.cc.keys()
        
    def train(self, item, cat):
        ''' trains our classifier '''
        features = self.getfeatures(item)
        # increment counters for all feature in category
        for f in features:
            self.incf(f, cat)
            
        # increment category counter
        self.incc(cat)
        
    def sample_train(ca):
        pass
        
    def fprob(self, f, cat):
        ''' evals probability for word be in category '''
        if self.catcount(cat) == 0: return 0
        return self.fcount(f, cat)/self.catcount(cat)
        
    def weightedprob(self, f, cat, prf, weight=1.0, ap=0.5):
        ''' weight our probability '''
        # evals current probability
        basicprob = prf(f, cat)
        
        # counts that feature in all category
        totals = sum([ self.fcount(f, c) for c in self.categories()])
        
        # evals middle value
        bp = (( weight*ap) + (totals*basicprob))/ (weight+totals)
        return bp
        
        
#Fisher classifier inherits main classifier

class fisher_classifier(classifier):
    ''' usage : weightedprob(word, cat, cprob) '''
    def __init__(self, getfeatures):
        classifier.__init__(self, getfeatures)
        self.minimums = {}
        
    def set_min(self, cat, min):
        self.minimums[cat] = min
        
    def get_min(self, cat):
        if cat not in self.minimums: return 0
        return self.minimums[cat]
    
    def cprob(self, f, cat):
        # clf frequency of feature in category
        clf = self.fprob(f, cat)
        if clf==0: return 0
        
        # freqsum frequency of feature in all categories
        freqsum = sum([self.fprob(f, c) for c in self.categories()])
        
        pr = clf/(freqsum)
        return pr
        
    def fisher_prob(self, item, cat):
        ''' classify document '''
        pr = 1
        features = self.getfeatures(item)
        for f in features:
            pr*= (self.weightedprob(f, cat, self.cprob))
            
        fscore = -2*math.log(pr)
        
        # use chi^2 function
        return self.invchi2(fscore, len(features)*2)
        
    def invchi2(self, chi, df):
        ''' chi^2 function for fisher probability '''
        m = chi/2.0
        sum = term = math.exp(-m)
        
        for i in range(1, df//2):
            term *= m/i
            sum += term
            
        return min(sum, 1.0)
        
    def classify(self, item, default=None):
        ''' search for best result '''
        best = default
        second = default
        max = 0.0
        
        for c in self.categories():
            p = self.fisher_prob(item, c)
            
            if p > self.get_min(c) and p > max:
                second = best
                best = c
                max = p
        return (best, second)
        