''' classificator for tags '''

import re
import math
import spec.external.Stemmer as stemmer
from read_tools import read

def getwords(doc):
    '''get simple features from string '''
    
    splitter = re.compile('\\W*')
    # split under non-literals
    words = [s.lower() for s in splitter.split(doc)
        if len(s) > 2 and len(s) < 20]
        
    #return array of unic words
#    return dict([ (w,1) for w in words])
    return words

def bookfeatures(doc, lang = "english"):
    ''' now available only for english books '''
    splitter = re.compile('\\W*')
    features = list()
    stem = stemmer.Stemmer(lang)
    
    words = [s.lower() for s in splitter.split(doc)
        if len(s) > 2 and len(s) < 20]

    res = {}
    for word in words:
        features.append(stem.stemWord(word))

    for i in xrange(len(features)):
        word = features[i]
        res[word] = 1
        if (i < len(words) - 1) and words[i] != words[i+1]:
            twowords = ' '.join(words[i:i+2])
            res[twowords] = 1
              
    return res

def bookfeatures2(doc):
    ''' now available only for english books '''
    splitter = re.compile('\\W*')
    features = []
    stem = stemmer.Stemmer('english')

    words = [s.lower() for s in splitter.split(doc)
        if len(s) > 2 and len(s) < 20]
        
    for i in xrange(len(words)):
        word = words[i]
        features.append(word)

    for word in words:
        word = stem.stemWord(word)
    
    return features

    
class Classifier:
    '''
    Classifier defines book genre using annotation.
    Kind of Bayes classifier.
    '''
    def __init__(self, getfeatures):
        #counter for feature/category
        self.feature_c = {}
        # document per category counter
        self.cathegory_c = {}
        # function that gets feature from document ( 'getwords' now )
        self.getfeatures = getfeatures
        
    def incf(self, feature, cat):
        ''' increments self.feature_c '''
        
        self.feature_c.setdefault(feature, {})
        self.feature_c[feature].setdefault(cat, 0)
        self.feature_c[feature][cat] += 1
        
    def incc(self, cat):
        ''' increments self.cathegory_c '''
        self.cathegory_c.setdefault(cat, 0)
        self.cathegory_c[cat] += 1
        
    def fcount(self, feature, cat):
        ''' counts feature in current category '''
        if feature in self.feature_c and cat in self.feature_c[feature]:
            return float(self.feature_c[feature][cat])
        return 0.0
        
    def catcount(self, cat):
        ''' counts docs for category '''
        if cat in self.cathegory_c:
            return float(self.cathegory_c[cat])
        return 0
    
    def totalcount(self):
        ''' total docs count '''
        return sum(self.cathegory_c.values())
        
    def categories(self):
        ''' total category count '''
        return self.cathegory_c.keys()
        
    def train(self, item, cat, lang = "english"):
        ''' trains our classifier '''
        features = self.getfeatures(item, lang)
        # increment counters for all feature in category
        for feature in features:
            self.incf(feature, cat)
            
        # increment category counter
        self.incc(cat)
               
    def fprob(self, feature, cat):
        ''' evals probability for word be in category '''
        if self.catcount(cat) == 0: 
            return 0
        return self.fcount(feature, cat)/self.catcount(cat)
        
    def weightedprob(self, feature, cat, prf, weight=1.0, apr=0.5):
        ''' weight our probability '''
        # evals current probability
        basicprob = prf(feature, cat)
        
        # counts that feature in all category
        totals = sum([ self.fcount(feature, cathegory) for cathegory in self.categories()])
        
        # evals middle value
        return (( weight*apr) + (totals*basicprob))/ (weight+totals)
        
        
#Fisher classifier inherits main classifier

class FisherClassifier(Classifier):
    ''' usage : weightedprob(word, cat, cprob) '''
    def __init__(self, getfeatures):
        Classifier.__init__(self, getfeatures)
        self.minimums = {}
        
    def set_min(self, cat, minimum):
        ''' set minimum probability for cathegory'''
        self.minimums[cat] = minimum
        
    def get_min(self, cat):
        ''' set minimum probability for cathegory'''        
        if cat not in self.minimums: 
            return 0
        return self.minimums[cat]
    
    def cprob(self, feature, cat):
        ''' evals probability for word be in category '''
        # clf frequency of feature in category
        clf = self.fprob(feature, cat)
        if clf == 0: 
            return 0
        
        # freqsum frequency of feature in all categories
        freqsum = sum([self.fprob(feature, c) for c in self.categories()])
        
        return clf/(freqsum)
        
    def fisher_prob(self, item, cat, lang="english"):
        ''' classify document '''
        prob = 1
        features = self.getfeatures(item, lang)
        for feature in features:
            coef = (self.weightedprob(feature, cat, self.cprob))
            prob *= coef
            if prob == 0:
                prob = 4.94065645841e-324            
        fscore = -2 * math.log(prob)

        # use chi^2 function
        return self.invchi2(fscore, len(features)*2)
        
    def invchi2(self, chi, df):
        ''' chi^2 function for fisher probability '''
        power = chi/2.0
        summ = term = math.exp(-power)
        
        for i in xrange(1, df//2):
            term *= power/i
            summ += term
            
        return min(summ, 1.0)
        
    def classify(self, item, default=None, lang="english"):
        ''' search for best result '''
        result = []
        authors_tags = default        
#        best = default
#        second = default
        maximum = 0.0
        
        for cathegory in self.categories():
            prob = self.fisher_prob(item, cathegory, lang)
#            print cathegory, prob
            if authors_tags:
                if cathegory in authors_tags:
                    prob += 1.0e-5
#                    print cathegory, prob
            if prob > self.get_min(cathegory) and prob > maximum:
                result.insert(0, cathegory)
#                second = best
#                best = cathegory
                maximum = prob
#        return (best, second)
        return result
    
    def sample_train(self):
        '''sample train on feedbooks '''
        # train on feedbooks
        for i in xrange(1, 153):
            read(False, 
            ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), self)

        # train on smashwords
#        for i in xrange(1, 10):
#            read_smashwords(False, ('http://www.smashwords.com/atom/books/1/popular/epub/any/0?page=%s' % i), self)

#        #train on www.allromanceebooks.com
#        for i in xrange(1, 10):
#            read_all_romance(False, ('http://www.allromanceebooks.com/epub-feed.xml?search=recent+additions;page=%s' % i), self)
            
        
